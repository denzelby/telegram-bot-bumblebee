package com.github.bumblebee.command.autocomplete.service;

import com.github.bumblebee.command.autocomplete.dao.AutoCompleteRepository;
import com.github.bumblebee.command.autocomplete.entity.AutoCompletePhrase;
import com.google.api.client.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AutoCompleteService {

    private AutoCompleteRepository repository;

    @Autowired
    public AutoCompleteService(AutoCompleteRepository repository) {
        this.repository = repository;
    }

    public AutoCompletePhrase storeAutoCompletePhrase(AutoCompletePhrase phrase) {
        return repository.save(phrase);
    }

    public void updateAutoCompletePhrase(String phraseKey,String phrasePattern) {
        repository.updatePhrase(phrasePattern,phraseKey);
    }

    public List<AutoCompletePhrase> retrieveAutoCompletePhrases() {
        return Lists.newArrayList(repository.findAll());
    }
}
