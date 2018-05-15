package com.github.bumblebee.command.autocomplete.service

import com.github.bumblebee.command.autocomplete.dao.AutoCompleteRepository
import com.github.bumblebee.command.autocomplete.entity.AutoCompletePhrase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AutoCompleteService(private val repository: AutoCompleteRepository) {
    fun storeAutoCompletePhrase(phrase: AutoCompletePhrase): AutoCompletePhrase = repository.save(phrase)

    fun updateAutoCompletePhrase(phraseKey: String, phrasePattern: String): Unit =
        repository.updatePhrase(phraseKey, phrasePattern)

    fun fetchCompletions(): Iterable<AutoCompletePhrase> = repository.findAll()
}
