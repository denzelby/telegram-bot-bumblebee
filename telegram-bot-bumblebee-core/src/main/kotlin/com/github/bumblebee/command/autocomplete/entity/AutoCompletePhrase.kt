package com.github.bumblebee.command.autocomplete.entity

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "BB_AUTOCOMPLETE_PHRASES")
class AutoCompletePhrase {

    @Id
    lateinit var phraseKey: String
    lateinit var phrasePattern: String

    @Suppress("unused")
    constructor()

    constructor(phraseKey: String, phrasePattern: String) {
        this.phraseKey = phraseKey
        this.phrasePattern = phrasePattern
    }

    override fun toString(): String = "AutoCompletePhrase(phraseKey=$phraseKey, phrasePattern=$phrasePattern)"
}
