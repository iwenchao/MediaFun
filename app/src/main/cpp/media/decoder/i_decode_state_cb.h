//
// Created by chaos on 2024/1/9.
//

#ifndef MEDIAFUN_I_DECODE_STATE_CB_H
#define MEDIAFUN_I_DECODE_STATE_CB_H

class  IDecoder;

class IDecoderStateCb{
public:
    IDecoderStateCb();
    virtual void DecodePrepare(IDecoder *decoder) = 0;
    virtual void DecodeStart(IDecoder *decoder) = 0;
    virtual void DecodeRunning(IDecoder *decoder) = 0;
    virtual bool DecodeOneFrame(IDecoder *decoder) = 0;
    virtual void DecodeFinish(IDecoder *decoder) = 0;
    virtual void DecodeStop(IDecoder *decoder) = 0;

};

#endif //MEDIAFUN_I_DECODE_STATE_CB_H
