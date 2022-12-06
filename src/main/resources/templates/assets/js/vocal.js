let Vocal = (function () {

    let User;
    let Conversation;
    let UserName;
    let UserLastname;
    let body = $('body');

    function init(ParamUser, ParamConversation, ParamUserName, ParamUserLastname) {
        User = ParamUser;
        Conversation = ParamConversation;
        UserName = ParamUserName;
        UserLastname = ParamUserLastname;
    }

    let startRecordingButtonPrime;
    let stopRecordingButtonPrime;
    let playButtonPrime;
    let sendButtonPrime;

    function clone() {
        startRecordingButtonPrime = document.getElementById('startRecordingButton').cloneNode(true);
        stopRecordingButtonPrime = document.getElementById('stopRecordingButton').cloneNode(true);
        playButtonPrime = document.getElementById('playButton').cloneNode(true);
        sendButtonPrime = document.getElementById('downloadButton').cloneNode(true);
    }

    function vocal() {

        let leftchannel = [];
        let rightchannel = [];
        let recorder = null;
        let recordingLength = 0;
        let volume = null;
        let mediaStream = null;
        let sampleRate = 44100;
        let context = null;
        let blob = null;

        body.on('click', '.start', function () {
            blob = null;
            leftchannel = [];
            rightchannel = [];
            // Initialize recorder
            navigator.getUserMedia(
                {
                    audio: true
                },
                function (e) {
                    console.log("user consent");

                    context = new AudioContext();

                    mediaStream = context.createMediaStreamSource(e);

                    let bufferSize = 2048;
                    let numberOfInputChannels = 2;
                    let numberOfOutputChannels = 2;
                    if (context.createScriptProcessor) {
                        recorder = context.createScriptProcessor(bufferSize, numberOfInputChannels, numberOfOutputChannels);
                    } else {
                        recorder = context.createJavaScriptNode(bufferSize, numberOfInputChannels, numberOfOutputChannels);
                    }

                    recorder.onaudioprocess = function (e) {
                        leftchannel.push(new Float32Array(e.inputBuffer.getChannelData(0)));
                        rightchannel.push(new Float32Array(e.inputBuffer.getChannelData(1)));
                        recordingLength += bufferSize;
                    }

                    mediaStream.connect(recorder);
                    recorder.connect(context.destination);

                    document.getElementById("vocal").innerHTML = "";
                    document.getElementById("vocal").appendChild(stopRecordingButtonPrime);

                },
                function (e) {
                    console.error(e);
                });
        });

        body.on('click', '.stop', function () {

            recorder.disconnect(context.destination);
            mediaStream.disconnect(recorder);

            let leftBuffer = flattenArray(leftchannel, recordingLength);
            let rightBuffer = flattenArray(rightchannel, recordingLength);
            let interleaved = interleave(leftBuffer, rightBuffer);

            let buffer = new ArrayBuffer(44 + interleaved.length * 2);
            let view = new DataView(buffer);

            writeUTFBytes(view, 0, 'RIFF');
            view.setUint32(4, 44 + interleaved.length * 2, true);
            writeUTFBytes(view, 8, 'WAVE');
            writeUTFBytes(view, 12, 'fmt ');
            view.setUint32(16, 16, true); // chunkSize
            view.setUint16(20, 1, true); // wFormatTag
            view.setUint16(22, 2, true); // wChannels: stereo (2 channels)
            view.setUint32(24, sampleRate, true); // dwSamplesPerSec
            view.setUint32(28, sampleRate * 4, true); // dwAvgBytesPerSec
            view.setUint16(32, 4, true); // wBlockAlign
            view.setUint16(34, 16, true); // wBitsPerSample
            writeUTFBytes(view, 36, 'data');
            view.setUint32(40, interleaved.length * 2, true);

            let index = 44;
            let volume = 1;
            for (let i = 0; i < interleaved.length; i++) {
                view.setInt16(index, interleaved[i] * (0x7FFF * volume), true);
                index += 2;
            }

            blob = new Blob([view], {type: 'audio/wav'});

            document.getElementById("vocal").innerHTML = "";
            document.getElementById("vocal").appendChild(startRecordingButtonPrime);
            document.getElementById("vocal").appendChild(playButtonPrime);
            document.getElementById("vocal").appendChild(sendButtonPrime);
        });

        body.on('click', '.play', function () {
            if (blob == null) {
                return;
            }

            let url = window.URL.createObjectURL(blob);
            let audio = new Audio(url);
            audio.play();
        });

        body.on('click', '.send', function () {
            if (blob == null) {
                return;
            }

            let file = new File([blob],getRandomString(20));
            Chat.sendFile(file);
        })
    }

    function getRandomString(length) {
        let randomChars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
        let result = '';
        for ( let i = 0; i < length; i++ ) {
            result += randomChars.charAt(Math.floor(Math.random() * randomChars.length));
        }
        return result;
    }

    function flattenArray(channelBuffer, recordingLength) {
        let result = new Float32Array(recordingLength);
        let offset = 0;
        for (let i = 0; i < channelBuffer.length; i++) {
            let buffer = channelBuffer[i];
            result.set(buffer, offset);
            offset += buffer.length;
        }
        return result;
    }

    function interleave(leftChannel, rightChannel) {
        let length = leftChannel.length + rightChannel.length;
        let result = new Float32Array(length);

        let inputIndex = 0;

        for (let index = 0; index < length;) {
            result[index++] = leftChannel[inputIndex];
            result[index++] = rightChannel[inputIndex];
            inputIndex++;
        }
        return result;
    }

    function writeUTFBytes(view, offset, string) {
        for (let i = 0; i < string.length; i++) {
            view.setUint8(offset + i, string.charCodeAt(i));
        }
    }

    return {
        vocal: vocal,
        clone: clone,
        init: init,
    }

}());