package jcats.util;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class SpeechAppender extends AppenderSkeleton {
	private static final Logger LOGGER = LoggerFactory.getLogger(SpeechAppender.class);
	private final Voice voice;
	public SpeechAppender(){
		String voiceName = "kevin16";
        VoiceManager voiceManager = VoiceManager.getInstance();
        voice = voiceManager.getVoice(voiceName);

        if (voice == null) {
        	LOGGER.error(
                "Cannot find a voice named "
                + voiceName + ".  Please specify a different voice.");
        }
        
        voice.allocate();
        voice.speak("Speech function enabled");
	}
	@Override
	public void close() {
		voice.deallocate();
	}

	@Override
	public boolean requiresLayout() {
		return false;
	}

	@Override
	protected void append(LoggingEvent arg0) {
		if (voice == null) return;
		voice.speak(arg0.getMessage().toString());
	}

}
