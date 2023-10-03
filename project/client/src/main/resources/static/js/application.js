$(function () {
    const wsUri = 'ws://localhost:8081/api';

    let audioContext,
        soundSource,
        bufferQueue = [];

/*
    document.addEventListener('visibilitychange', function () {
       if (audioContext) {
           if (document.hidden) {
               audioContext.suspend().then();
           } else {
               audioContext.resume();
           }
       }
    });
*/
    document.getElementById('start').addEventListener('click', function (){
        connect();
    });

    document.getElementById('stop').addEventListener('click', function (){
        disconnect();
    });

    if (window.WebSocket === undefined)
        throw new Error("WebSockets not supported.");

    let websocket;

    function connect() {
        if (websocket !== undefined && websocket.readyState !== WebSocket.CLOSED)
            return;

        try {
            audioContext = new AudioContext();
            audioContext.resume().then();
        } catch (e) {
            throw new Error("Web Audio API is not supported in this browser");
        }

        websocket = new WebSocket(wsUri);
        websocket.binaryType = "arraybuffer";

        websocket.onopen = function (event) {
            onOpen(event)
        };
        websocket.onclose = function (event) {
            onClose(event)
        };
        websocket.onmessage = function (event) {
            onMessage(event)
        };
        websocket.onerror = function (event) {
            onError(event)
        };
    }

    function disconnect() {
        if (websocket) {
            websocket.close();
            websocket = undefined;
            stopSound();
        }
    }

    function onOpen(event) {
        console.log(event);
    }

    function onClose(event) {
        console.log(event);
    }

    // С БУФФЕРОМ !!!
    const decodeFunc = function () {
        if (bufferQueue.length > 0) {
            const soundBufferIn = bufferQueue[0];
            audioContext.decodeAudioData(soundBufferIn, function (soundBuffer) {
                const soundSource = audioContext.createBufferSource();
                // Link the Sound to the Output
                soundSource.connect(audioContext.destination);

                // Add the buffered data to our object
                soundSource.buffer = soundBuffer;
                soundSource.onended = function () {
                    bufferQueue.shift();
                    if (bufferQueue.length > 0) {
                        decodeFunc();
                    }
                };
                //console.log('Duration: ' + soundBuffer.duration + ', startTime: ' + startTime);
                soundSource.start();
            }, function (e) {
                console.log(e);
            }).then();
        }
    };

    function onMessage(event) {
        //console.log(event);
        if (event.data instanceof ArrayBuffer) {
            if (event.data.byteLength === 0) {
                //console.log("Empty arrayBuffer");
            } else if(event.data.byteLength >= 50 && audioContext.state === 'running') {
                if (bufferQueue.length < 3) {
                    if (bufferQueue.length > 0) {
                        bufferQueue.push(event.data);
                    } else {
                        bufferQueue.push(event.data);
                        decodeFunc();
                    }
                }
            }
        } else {
            console.log(event);
        }
    }


    function onError(event) {
        console.log(event);
    }

    function stopSound() {
        if(soundSource) {
            soundSource.stop();
            soundSource.disconnect();
            //console.log("Sound stopped. AudioContext state: " + audioContext.state);
            bufferQueue = [];
        }
    }
});
