//
// Created by chaos on 2024/1/9.
//

#ifndef MEDIAFUN_I_DECODER_H
#define MEDIAFUN_I_DECODER_H


class IDecoder{
public:
    virtual void goOn() = 0;
    virtual void Pause() = 0;
    virtual void Stop() = 0;
    virtual bool IsRunning() =0;
    virtual long GetDuration() = 0;
    virtual void SetStateReceiver() = 0;
};



#endif //MEDIAFUN_I_DECODER_H
