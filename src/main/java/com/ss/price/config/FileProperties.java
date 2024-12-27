package com.ss.price.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "file")
public class FileProperties {

    private PathConfig path;

    public PathConfig getPath() {
        return path;
    }

    public void setPath(PathConfig path) {
        this.path = path;
    }

    public static class PathConfig {
        private String windows;
        private String linux;

        public String getWindows() {
            return windows;
        }

        public void setWindows(String windows) {
            this.windows = windows;
        }

        public String getLinux() {
            return linux;
        }

        public void setLinux(String linux) {
            this.linux = linux;
        }
    }

    public String getSavePath() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            System.out.println("当前环境是：win");
            return path.getWindows();
        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            System.out.println("当前环境是：linux");
            return path.getLinux();
        } else {
            throw new IllegalStateException("Unsupported operating system: " + os);
        }
    }
}