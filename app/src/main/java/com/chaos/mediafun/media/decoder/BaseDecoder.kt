package com.chaos.mediafun.media.decoder

import android.media.MediaCodec
import android.media.MediaFormat
import android.util.Log
import com.chaos.mediafun.media.DecodeState
import com.chaos.mediafun.media.Frame
import com.chaos.mediafun.media.IDecoder
import com.chaos.mediafun.media.IDecoderProgress
import com.chaos.mediafun.media.IDecoderStateListener
import com.chaos.mediafun.media.IExtractor
import java.io.File
import java.nio.ByteBuffer

/**
 * @Author      : wen
 * @Email       : iwenchaos6688@163.com
 * @Date        : on 2023/12/26 09:27.
 * @Description :描述
 */
abstract class BaseDecoder(private val filePath: String) : IDecoder {
    private val TAG = "TAG"

    //------------线程相关------------------
    private var isRunning = true
    private val lock = Object()

    //------------状态相关----------------
    protected var decoderStateListener: IDecoderStateListener? = null

    /**
     * 解码器
     */
    private var codec: MediaCodec? = null
    private var state = DecodeState.STOP
    private var extractor: IExtractor? = null

    private var inputBuffers: Array<ByteBuffer>? = null
    private var outputBuffer: Array<ByteBuffer>? = null

    /**
     * 解码数据信息
     */
    private var bufferInfo = MediaCodec.BufferInfo()
    private var isEOS = false
    protected var videoWidth = 0

    protected var videoHeight = 0

    private var duration: Long = 0

    private var startPos: Long = 0

    private var endPos: Long = 0

    /**
     * 开始解码时间，用于音视频同步
     */
    private var startTimeForSync = -1L

    // 是否需要音视频渲染同步
    private var syncRender = true

    override fun run() {
        if (state == DecodeState.STOP) {
            state = DecodeState.START
        }
        decoderStateListener?.decoderPrepare(this)
        //解码第一步：初始化，启动解码器
        if (!init()) {
            Log.i("TAG","init 错误")
            return
        }
        try {
            Log.i("TAG","开始循环解码...")
            while (isRunning) {
                if (state != DecodeState.START
                    && state != DecodeState.DECODING
                    && state != DecodeState.SEEKING
                ) {
                    Log.i("TAG","进入等待 ,waitDecode")
                    //进入等待
                    waitDecode()
                    //同步时间，即恢复同步的起始时间。（需要除去等待流失的时间）
                    startTimeForSync = System.currentTimeMillis() - getCurTimeStamp()
                }

                if (!isRunning || state == DecodeState.STOP) {
                    isRunning = false
                    break
                }

                if (startTimeForSync == -1L) {
                    startTimeForSync = System.currentTimeMillis()
                }

                //如果数据没有解码完毕，将数据推入解码器解码
                if (!isEOS) {
                    isEOS = pushBufferToDecoder()
                }

                //解码步骤3：将解码好的数据从缓冲区拉取出来
                val index = pullBufferFromDecoder()
                if (index >= 0) {
                    //音视频同步
                    if(syncRender && state == DecodeState.DECODING){
                        sleepRender()
                    }
                    //解码步骤4：将解码好的数据渲染到渲染器中
                    if(syncRender){
                        render(outputBuffer!![index],bufferInfo!!)
                    }
                    //将解码数据传递出去
                    val frame = Frame()
                    frame.buffer = outputBuffer!!.get(index)
                    frame.bufferInfo = bufferInfo!!
                    decoderStateListener?.decodeOneFrame(this,frame)
                    //解码步骤5：将解码器中数据释放
                    codec?.releaseOutputBuffer(index,true)
                    if(state == DecodeState.START){
                        state = DecodeState.PAUSE
                    }
                }else{
                    Log.i("TAG","解码器没有数据${this.javaClass}")
                }
                //解码步骤6：判断是否需要继续解码
                if(bufferInfo!!.flags == MediaCodec.BUFFER_FLAG_END_OF_STREAM){
                    state = DecodeState.FINISH
                    decoderStateListener?.decoderFinish(this)
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }finally {
            doneEncode()
            release()
        }
    }

    private fun init(): Boolean {
        if (filePath.isNullOrEmpty() || !File(filePath).exists()) {
            Log.i("TAG","文件路径有问题 ${filePath}")
            decoderStateListener?.decoderError(this, "文件路径为空")
            return false
        }

        if (!check()) {
            Log.i("TAG","check错误")
            return false
        }

        //初始化数据提取器
        extractor = initExtractor(filePath)
        if (extractor!!.getFormat() == null) {
            Log.i("TAG","initExtractor错误")
            return false
        }

        //初始化参数
        if (!initParams()) {
            Log.i("TAG","initParams 错误")
            return false
        }
        if (!initRender()){
            Log.i("TAG","initRender 错误")
            return false
        }
        if(!initCodec()){
            Log.i("TAG","initCodec 错误")
            return false
        }
        return true
    }

    private fun initParams(): Boolean {
        try {
            val format = extractor!!.getFormat()
            duration = format!!.getLong(MediaFormat.KEY_DURATION)
            if (endPos == 0L) {
                endPos = duration
            }
            initSpecParams(format)
        } catch (e: Exception) {
            return false
        }
        return true
    }
    private fun initCodec(): Boolean {
        try {
            val mime = extractor!!.getFormat()!!.getString(MediaFormat.KEY_MIME)
            codec = MediaCodec.createDecoderByType(mime!!)
            if(!configCodec(codec!!,extractor!!.getFormat()!!)){
                Log.i("TAG","configCodec 错误,waitDecode")
                waitDecode()
            }
            codec!!.start()
            inputBuffers = codec?.inputBuffers
            outputBuffer = codec?.outputBuffers
        }catch (e:Exception){
            return false
        }
        return true
    }

    private fun waitDecode() {
        try {
            if (state == DecodeState.PAUSE) {
                decoderStateListener?.decoderPause(this)
            }
            synchronized(lock){
                lock.wait()
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    protected fun notifyDecode() {
        try {
            synchronized(lock){
                lock.notifyAll()
            }
            if (state == DecodeState.DECODING) {
                decoderStateListener?.decoderRunning(this)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun pushBufferToDecoder(): Boolean {
        var inputBufferIndex = codec!!.dequeueInputBuffer(1000)
        var isEndOfStream = false
        if (inputBufferIndex > 0) {
            val inputBuffer = inputBuffers!![inputBufferIndex]
            val sampleSize = extractor!!.readBuffer(inputBuffer)
            if (sampleSize < 0) {
                codec!!.queueInputBuffer(
                    inputBufferIndex,
                    0,
                    0,
                    0,
                    MediaCodec.BUFFER_FLAG_END_OF_STREAM
                )
                isEndOfStream = true
            } else {
                codec!!.queueInputBuffer(
                    inputBufferIndex,
                    0,
                    sampleSize,
                    extractor!!.getCurrentTimeStamp(),
                    0
                )
            }
        }
        return isEndOfStream
    }

    private fun pullBufferFromDecoder(): Int {
        //查询是否有解码完成的数据，index>0表示有数据,并且index为数据缓存索引
        val index = codec!!.dequeueOutputBuffer(bufferInfo, 1000)
        when (index) {
            MediaCodec.INFO_OUTPUT_FORMAT_CHANGED,
            MediaCodec.INFO_TRY_AGAIN_LATER -> {}
            MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED -> {
                outputBuffer = codec!!.getOutputBuffers()
            }
            else -> {
                return index
            }
        }
        return -1
    }

    private fun sleepRender(){
        val passTime = System.currentTimeMillis() - startTimeForSync
        val curTime = getCurTimeStamp()
        if(curTime > passTime){
            Thread.sleep(curTime - passTime)
//            Log.i(TAG, "sleepRender: ${curTime - passTime}")
        }
    }

    private fun release(){
        try {
            Log.i(TAG, "解码停止，释放解码器")
            state = DecodeState.STOP
            isEOS = false
            extractor?.stop()
            codec?.stop()
            codec?.release()
            decoderStateListener?.decoderDestroy(this)
        } catch (e: Exception) {
        }
    }

    override fun pause() {
        state = DecodeState.PAUSE
    }

    override fun goOn() {
        state = DecodeState.DECODING
        notifyDecode()
    }

    override fun seekTo(pos: Long): Long {
        return 0
    }

    override fun seekAndPlay(pos: Long): Long {
        return 0
    }

    override fun stop() {
       state = DecodeState.STOP
        isRunning = false
        notifyDecode()
    }

    override fun isDecoding(): Boolean {
        return state == DecodeState.DECODING
    }

    override fun isSeeking(): Boolean {
        return state == DecodeState.SEEKING
    }

    override fun isStop(): Boolean {
        return state == DecodeState.STOP
    }

    override fun setSizeListener(l: IDecoderProgress) {
    }

    override fun setStateListener(l: IDecoderStateListener) {
        decoderStateListener = l
    }

    override fun getWidth(): Int {
        return videoWidth
    }

    override fun getHeight(): Int {
        return videoHeight
    }

    override fun getDuration(): Long {
        return duration
    }

    override fun getCurTimeStamp(): Long {
        return bufferInfo!!.presentationTimeUs / 1000
    }

    override fun getRotationAngle(): Int {
        return 0
    }

    override fun getMediaFormat(): MediaFormat? {
        return extractor?.getFormat()
    }

    override fun getTrack(): Int {
        return 0
    }

    override fun getFilePath(): String {
        return filePath
    }

    override fun withoutSync(): IDecoder {
        syncRender = false
        return this
    }

    /**
     * 检查子类参数
     */
    abstract fun check(): Boolean

    /**
     * 音视频数据提取器
     */
    abstract fun initExtractor(path: String): IExtractor

    /**
     * 初始化子类特有的参数
     */
    abstract fun initSpecParams(format: MediaFormat)

    /**
     * 配置解码器
     */
    abstract fun configCodec(codec: MediaCodec, format: MediaFormat): Boolean

    /**
     * 初始化渲染器
     */
    abstract fun initRender(): Boolean

    /**
     * 渲染数据
     */
    abstract fun render(outputBuffer: ByteBuffer, info: MediaCodec.BufferInfo)

    /**
     * 结束解码
     */
    abstract fun doneEncode()
}