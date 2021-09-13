package com.imorning.mediaplayer.player;


import java.io.File;

public abstract class Player {
    static {
        System.loadLibrary("Player");
    }

    protected File dataSourceFile;


    /**
     * play media
     */
    public abstract void play();

    /**
     * pause
     */
    public abstract void pause();

    /**
     * stop Play and release resource
     */
    public abstract void stop();

    /**
     * seek to time
     *
     * @param time time format to seconds
     */
    public abstract void seekTo(long time);

    public String getFilePath() {
        return dataSourceFile.getAbsolutePath();
    }

    public void setFilePath(String filePath) {
        dataSourceFile = new File(filePath);
    }

    public String getMediaInfo() {
        if (dataSourceFile != null) {
            return nativeGetMediaInfo(dataSourceFile.getAbsolutePath());
        }
        return null;
    }

    private native String nativeGetMediaInfo(String dataSourceFileAbsolutePath);

}
