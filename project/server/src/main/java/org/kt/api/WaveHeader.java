package org.kt.api;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class WaveHeader {
    protected static final int HEADER_SIZE = 44;

    public static byte[] build(int numChannels, int sampleRate, int bitsPerSample, int numSamples) throws IOException {
        int subchunk2Size = numSamples * numChannels * bitsPerSample / 8;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(HEADER_SIZE);
        DataOutputStream out = new DataOutputStream(baos);

        out.writeBytes("RIFF");                                     // WAV chunk header
        out.writeInt(Integer.reverseBytes(subchunk2Size + 36));
        out.writeBytes("WAVE");                                     // WAV format
        out.writeBytes("fmt ");                                     // format subchunk header
        out.writeInt(Integer.reverseBytes(16));                     // format subchunk size
        out.writeShort(Short.reverseBytes((short) 1));              // audio format
        out.writeShort(Short.reverseBytes((short) numChannels));    // number of channels
        out.writeInt(Integer.reverseBytes(sampleRate));             // sample rate
        out.writeInt(Integer.reverseBytes(sampleRate * numChannels * bitsPerSample / 8));       // byte rate
        out.writeShort(Short.reverseBytes((short) (numChannels * bitsPerSample / 8)));          // block align
        out.writeShort(Short.reverseBytes((short) bitsPerSample));  // bits per sample
        out.writeBytes("data");                                     // data subchunk header
        out.writeInt(Integer.reverseBytes(subchunk2Size));          // data subchunk size

        out.flush();

        return baos.toByteArray();
    }
}
