
public class ProgressHandler {
    
    private int progress;
    private boolean ended;
    private boolean processing;
    private boolean started;
    
    public ProgressHandler() {
    }
    
    public void setProgress(int progress) {
        setProcessing(true);
        this.progress = progress;
    }
    
    public void setEnded(boolean ended) {
        if (ended = true)
            started = false;
            
        this.ended = ended;
    }
    
    public void setStarted(boolean started) {
        if (started = true)
            ended = false;
        
        this.started = started;
    }

    public void setProcessing(boolean processing) {
        setStarted(true);
        this.processing = processing;
    }
    
    public int getProgress() {
        return progress;
    }
    
    public boolean isEnded() {
        return ended;
    }
    
    public boolean isStarted() {
        return started;
    }
    
    public boolean isProcessing() {
        return processing;
    }
}
