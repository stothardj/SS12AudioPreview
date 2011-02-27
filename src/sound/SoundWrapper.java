package sound;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC10;
import org.lwjgl.util.WaveData;

public class SoundWrapper {
	private int numSources;
	private int curNumSources;
	private IntBuffer buffer;
	private IntBuffer source;
	private AudioSource[] sources;
	private AudienceMember member;
	public SoundWrapper(int numSources) {
		try{
			AL.create();
		} catch (LWJGLException le) {
			le.printStackTrace();
			return;
		}
		AL10.alGetError();
		this.curNumSources = 0;
		this.member = new AudienceMember((FloatBuffer) BufferUtils
				.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }), (FloatBuffer) BufferUtils
				.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }), (FloatBuffer) BufferUtils
				.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }));
		if(numSources > 0) {
			this.numSources = numSources;
			sources = new AudioSource[numSources];
			this.buffer = BufferUtils.createIntBuffer(numSources);
			this.source = BufferUtils.createIntBuffer(numSources);
			AL10.alDistanceModel(AL10.AL_INVERSE_DISTANCE_CLAMPED);
			AL10.alGetError();
			AL10.alGenBuffers(this.buffer);
			if (AL10.alGetError() != AL10.AL_NO_ERROR) {
				return;
			}
			AL10.alGenSources(this.source);
			if (AL10.alGetError() != AL10.AL_NO_ERROR) {
				return;
			}
		}
		else {
			this.numSources = 0;
		}
	}
	
	public boolean initializeSource(String fileName, boolean loop, boolean skip) {
		if(this.curNumSources >= numSources) {
			return false;
		}
		else {
		sources[this.curNumSources] = new AudioSource((FloatBuffer) BufferUtils
				.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }), (FloatBuffer) BufferUtils
				.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }));
		WaveData waveFile = WaveData.create(fileName);
		if(waveFile == null) {
			return false;
		}
		sources[this.curNumSources].setPlaying(false);
		if(skip) {
			sources[this.curNumSources].setSkip(skip);
		}
		AL10.alBufferData(buffer.get(curNumSources), waveFile.format, waveFile.data, waveFile.samplerate);
		waveFile.dispose();
		  AL10.alSourcei(source.get(curNumSources), AL10.AL_BUFFER,   buffer.get(curNumSources));
		  AL10.alSourcef(source.get(curNumSources), AL10.AL_PITCH,    1.0f          );
		  AL10.alSourcef(source.get(curNumSources), AL10.AL_GAIN,     1.0f          );
		  AL10.alSource (source.get(curNumSources), AL10.AL_POSITION, (FloatBuffer) sources[this.curNumSources].getSourcePos());
		  AL10.alSource (source.get(curNumSources), AL10.AL_VELOCITY, (FloatBuffer) sources[this.curNumSources].getSourceVel());
		  if(loop)
			  AL10.alSourcei(source.get(curNumSources), AL10.AL_LOOPING,  AL10.AL_TRUE  );
		  else
			  AL10.alSourcei(source.get(curNumSources), AL10.AL_LOOPING,  AL10.AL_FALSE  );
		  curNumSources++;
		}
		return true;
	}
	
	 
	public void setSourceSPos(int sourceIndex, int coordIndex, float value) {
		if (sourceIndex >= numSources || coordIndex >= 3 || sourceIndex < 0 || coordIndex < 0) {
			System.err.println("ERROR");
			return;
		}
		else {
			sources[sourceIndex].setSourceSPos(coordIndex, value);
			AL10.alSource (source.get(sourceIndex), AL10.AL_POSITION, (FloatBuffer) sources[sourceIndex].getSourcePos());
		}
	}
	public void setSourceSVel(int sourceIndex, int coordIndex, float value) {
		if (sourceIndex >= numSources || coordIndex >= 3 || sourceIndex < 0 || coordIndex < 0) {
			return;
		}
		else {
			sources[sourceIndex].setSourceSVel(coordIndex, value);
			AL10.alSource (source.get(sourceIndex), AL10.AL_VELOCITY, (FloatBuffer) sources[sourceIndex].getSourceVel());
		}
	}
	public void setListenerSPos(int coordIndex, float value) {
		if (coordIndex >= 3 || coordIndex < 0) {
			return;
		}
		else {
			member.setListenerSPos(coordIndex, value);
			AL10.alListener(AL10.AL_POSITION, member.getListenerPos());
		}
	}
	public void setListenerSVel(int coordIndex, float value) {
		if (coordIndex >= 3 || coordIndex < 0) {
			return;
		}
		else {
			member.setListenerSVel(coordIndex, value);
			AL10.alListener(AL10.AL_VELOCITY, member.getListenerVel());
		}
	}
	public void setListenerSOri(int coordIndex, float value) {
		if (coordIndex >= 6 || coordIndex < 0) {
			return;
		}
		else {
			member.setListenerSOri(coordIndex, value);
			AL10.alListener(AL10.AL_ORIENTATION, member.getListenerOri());
		}
	}
	public void setSourcePos(int sourceIndex, FloatBuffer buffer) {
		if (sourceIndex >= numSources || sourceIndex < 0 || buffer == null) {
			return;
		}
		else {
			sources[sourceIndex].setSourcePos(buffer);
			AL10.alSource (source.get(sourceIndex), AL10.AL_POSITION, (FloatBuffer) sources[sourceIndex].getSourcePos());
		}		
	}
	public void setSourceVel(int sourceIndex, FloatBuffer buffer) {
		if (sourceIndex >= numSources || sourceIndex < 0 || buffer == null) {
			return;
		}
		else {
			sources[sourceIndex].setSourceVel(buffer);
			AL10.alSource (source.get(sourceIndex), AL10.AL_VELOCITY, (FloatBuffer) sources[sourceIndex].getSourceVel());
		}		
	}
	public void setListenerPos(FloatBuffer buffer) {
		if (buffer == null) {
			return;
		}
		else {
			member.setListenerPos(buffer);
			AL10.alListener(AL10.AL_POSITION, member.getListenerPos());
		}		
	}
	public void setListenerVel(FloatBuffer buffer) {
		if (buffer == null) {
			return;
		}
		else {
			member.setListenerVel(buffer);
			AL10.alListener(AL10.AL_VELOCITY, member.getListenerVel());
		}		
	}
	
	void killALData() {
		AL10.alDeleteSources(source);
		AL10.alDeleteBuffers(buffer);
	}
	
	public void play() {
		for(int i = 0; i < this.curNumSources; i++) {
			if(sources[i].isSkipped() != true) {
			sources[i].setPlaying(true);
			AL10.alSourcePlay(source.get(i));
			}
		}
	}
	public void singlePlay(int sourceIndex) {
		if(sourceIndex < this.curNumSources && sourceIndex >= 0) {
				AL10.alSourcePlay(source.get(sourceIndex));
		}
	}
	public void stop() {
		for(int i = 0; i < this.curNumSources; i++) {
			sources[i].setPlaying(false);
			AL10.alSourceStop(source.get(i));
		}
	}
	public void singleStop(int sourceIndex) {
		if(sourceIndex < this.curNumSources && sourceIndex >= 0) {
			sources[sourceIndex].setPlaying(false);
			AL10.alSourceStop(source.get(sourceIndex));
		}
	}
	public void pause() {
		for(int i = 0; i < this.curNumSources; i++) {
			if(sources[i].isSkipped() != true) {
				sources[i].setPlaying(false);
				AL10.alSourcePause(source.get(i));
			}
		}
	}
	public void singlePause(int sourceIndex) {
		if(sourceIndex < this.curNumSources && sourceIndex >= 0 && sources[sourceIndex].isSkipped() != true) {
			sources[sourceIndex].setPlaying(false);
			AL10.alSourcePause(source.get(sourceIndex));
		}
	}
	public boolean areAllPlaying() {
		boolean boolRet = true;
		for(int i = 0; i < this.curNumSources; i ++) {
			if(!sources[i].isSkipped())
				boolRet = boolRet && sources[i].isPlaying();
		}
		return boolRet;
	}
	public boolean isSourcePlaying(int sourceIndex) {
		if(sourceIndex < this.curNumSources) {
			return sources[sourceIndex].isPlaying();
		}
		else {
			return false;
		}
	}
	public void setSourceSkip(int sourceIndex) {
		if(sourceIndex < this.curNumSources) {
			sources[sourceIndex].setSkip(true);
		}
	}
	public boolean isSourceSkipping(int sourceIndex) {
		if(sourceIndex < this.curNumSources) {
			return sources[sourceIndex].isSkipped();
		}
		else {
			return false;
		}
	}
}
