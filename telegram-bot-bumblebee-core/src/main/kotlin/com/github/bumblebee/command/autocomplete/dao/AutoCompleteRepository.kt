package com.github.bumblebee.command.autocomplete.dao

import com.github.bumblebee.command.autocomplete.entity.AutoCompletePhrase
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface AutoCompleteRepository : CrudRepository<AutoCompletePhrase, String> {

    @Modifying
    @Query("UPDATE AutoCompletePhrase phrase SET phrase.phrasePattern = :phrasePattern " +
            "WHERE phrase.phraseKey = :phraseKey")
    fun updatePhrase(@Param("phraseKey") phraseKey: String, @Param("phrasePattern") phrasePattern: String)
}
