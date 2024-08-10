package ui.page;

// Represents a Page that must have its contents preloaded or reloaded before being switched to
public interface PreLoadedContentPage {
    void runBeforeSwitch();
}
