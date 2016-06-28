package com.github.bumblebee.command.autocomplete.entity;

import javax.persistence.*;

@Entity
@Table(name = "AUTOCOMPLETE_PHRASES")
public class AutoCompletePhrase {

    @Id
    private String phraseKey;

    private String phrasePattern;

    public AutoCompletePhrase() {

    }

    public AutoCompletePhrase(String phraseKey, String phrasePattern) {
        this.phraseKey = phraseKey;
        this.phrasePattern = phrasePattern;
    }

    public void setPhraseToComplete(String phraseKey) {
        this.phraseKey = phraseKey;
    }

    public void setCompletePhrase(String phrasePattern) {
        this.phrasePattern = phrasePattern;
    }

    public String getPhraseKey() {
        return phraseKey;
    }

    public String getPhrasePattern() {
        return phrasePattern;
    }

    @Override
    public String toString() {
        return "PhraseKey:" + phraseKey
                + " PhrasePattern:" + phrasePattern;
    }
}
