package audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.ByteArrayOutputStream;
import java.util.EnumMap;
import java.util.Map;

public class SoundManager {

    public enum Sound {
        START,
        CHOMP,
        FRUIT,
        EAT_GHOST,
        DEATH,
        WIN
    }

    private static final float SAMPLE_RATE = 22050f;

    private final Map<Sound, Clip> clips = new EnumMap<>(Sound.class);

    public SoundManager() {
        load(Sound.START, startJingle());
        load(Sound.CHOMP, chomp());
        load(Sound.FRUIT, fruit());
        load(Sound.EAT_GHOST, eatGhost());
        load(Sound.DEATH, death());
        load(Sound.WIN, win());
    }

    public void play(Sound sound) {

        Clip clip = clips.get(sound);

        if (clip == null) {
            return;
        }

        clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }

    public void playIfIdle(Sound sound) {

        Clip clip = clips.get(sound);

        if (clip != null && !clip.isRunning()) {
            play(sound);
        }
    }

    public void stopAll() {
        for (Clip clip : clips.values()) {
            clip.stop();
        }
    }

    private void load(Sound sound, byte[] data) {

        try {
            AudioFormat format = new AudioFormat(SAMPLE_RATE, 16, 1, true, false);
            Clip clip = AudioSystem.getClip();
            clip.open(format, data, 0, data.length);
            clips.put(sound, clip);
        } catch (Exception | LinkageError e) {
            System.out.println("Sound unavailable: " + sound);
        }
    }

    private byte[] startJingle() {
        Tone t = new Tone();
        int[] notes = {494, 988, 740, 622, 988, 740, 622, 523, 1047, 784, 659, 1047, 784, 659};
        for (int note : notes) {
            t.square(note, note, 110, 0.18);
        }
        return t.bytes();
    }

    private byte[] chomp() {
        Tone t = new Tone();
        t.triangle(500, 250, 70, 0.35);
        t.triangle(250, 500, 70, 0.35);
        return t.bytes();
    }

    private byte[] fruit() {
        Tone t = new Tone();
        t.square(300, 1200, 250, 0.18);
        return t.bytes();
    }

    private byte[] eatGhost() {
        Tone t = new Tone();
        t.square(200, 1600, 300, 0.18);
        return t.bytes();
    }

    private byte[] death() {
        Tone t = new Tone();
        for (int i = 0; i < 8; i++) {
            int top = 900 - i * 90;
            t.square(top, top - 250, 120, 0.18);
        }
        t.square(300, 80, 250, 0.18);
        return t.bytes();
    }

    private byte[] win() {
        Tone t = new Tone();
        int[] notes = {523, 659, 784, 1047, 784, 1047};
        for (int note : notes) {
            t.square(note, note, 150, 0.18);
        }
        return t.bytes();
    }

    private static class Tone {

        private final ByteArrayOutputStream out = new ByteArrayOutputStream();
        private double phase = 0;

        void square(double fromHz, double toHz, int ms, double volume) {
            add(fromHz, toHz, ms, volume, true);
        }

        void triangle(double fromHz, double toHz, int ms, double volume) {
            add(fromHz, toHz, ms, volume, false);
        }

        private void add(double fromHz, double toHz, int ms, double volume, boolean square) {

            int samples = (int) (SAMPLE_RATE * ms / 1000);

            for (int i = 0; i < samples; i++) {

                double progress = (double) i / samples;
                double hz = fromHz + (toHz - fromHz) * progress;
                phase = (phase + hz / SAMPLE_RATE) % 1.0;

                double wave = square
                        ? (phase < 0.5 ? 1 : -1)
                        : 1 - 4 * Math.abs(phase - 0.5);

                int edge = Math.min(i, samples - 1 - i);
                double fade = Math.min(1.0, edge / (SAMPLE_RATE * 0.004));

                short value = (short) (wave * volume * fade * Short.MAX_VALUE);
                out.write(value & 0xFF);
                out.write((value >> 8) & 0xFF);
            }
        }

        byte[] bytes() {
            return out.toByteArray();
        }
    }
}
