package com.aryanwalia.imagetotextconversion;

public class LanguageData {
    private String complex_word;
    private String hindi;
    private String punjabi;
    private String gujarati;
    private String arabic;

    public String getComplex_word() {
        return complex_word;
    }

    public void setComplex_word(String complex_word) {
        this.complex_word = complex_word;
    }

    public String getHindi() {
        return hindi;
    }

    public void setHindi(String hindi) {
        this.hindi = hindi;
    }

    public String getPunjabi() {
        return punjabi;
    }

    public void setPunjabi(String punjabi) {
        this.punjabi = punjabi;
    }

    public String getGujarati() {
        return gujarati;
    }

    public void setGujarati(String gujarati) {
        this.gujarati = gujarati;
    }

    public String getArabic() {
        return arabic;
    }

    public void setArabic(String arabic) {
        this.arabic = arabic;
    }

    @Override
    public String toString() {
        return "Complex Word: "+complex_word+"      "+"Hindi: "+hindi+"    "+"Punjabi: "+punjabi+"    "+"Gujarati: "+gujarati+"     "+"Arabic: "+arabic;
    }
}
