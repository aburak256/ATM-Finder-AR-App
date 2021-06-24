package com.example.myapplication.models;

public class AuthTokenRequest {

    private String appVersion= "1.0.0";
    private String localeVersion= "vqovgPCe+9XNkiE7zwHww+74aY9T0i3+mjdy4XZ/O8Y\\u003d";
    private Device device;

    public String getAppVersion() {
        return appVersion;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public static class Device {
        boolean isRooted = false;
        String make;
        String model;
        private Os os;

        public boolean isRooted() {
            return isRooted;
        }

        public void setRooted(boolean rooted) {
            isRooted = rooted;
        }

        public String getMake() {
            return make;
        }

        public void setMake(String make) {
            this.make = make;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public Os getOs() {
            return os;
        }

        public void setOs(Os os) {
            this.os = os;
        }

        public static class Os{
            String family;
            String version;

            public String getFamily() {
                return family;
            }

            public void setFamily(String family) {
                this.family = family;
            }

            public String getVersion() {
                return version;
            }

            public void setVersion(String version) {
                this.version = version;
            }
        }
    }

}
