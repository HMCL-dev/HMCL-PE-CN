package cosine.boat;

public interface BoatLaunchCallback {
    void onStart();
    void onError(Exception e);
    void onExit(int code);
}
