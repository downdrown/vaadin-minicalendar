window.getUserPreferredColorScheme = function() {
    return matchMedia("(prefers-color-scheme: dark)").matches ? "dark" : "light";
}
