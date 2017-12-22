package com.github.bumblebee.command;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.telegram.domain.Chat;
import com.github.telegram.domain.Message;
import com.github.telegram.domain.Update;

@RunWith(MockitoJUnitRunner.class)
public class SingleArgumentCommandTest {

    @Mock
    private Update update;

    @Mock
    private Message message;

    @Mock
    private Chat chat;

    @Spy
    @InjectMocks
    private SingleArgumentCommand command = new SingleArgumentCommand() {
        @Override
        public void handleCommand(@NotNull Update update, long chatId, String argument) {

        }
    };

    @Before
    public void setUp() {
        when(update.getMessage()).thenReturn(message);
        when(message.getChat()).thenReturn(chat);
        when(chat.getId()).thenReturn(42L);
    }

    @Test
    public void shouldPassValidSingleArgument() throws Exception {

        // given
        when(message.getText()).thenReturn("/g cats");

        // when
        command.onUpdate(update);

        // then
        verify(command).handleCommand(update, 42L, "cats");
    }

    @Test
    public void shouldPassEmptyArgument() throws Exception {

        // given
        when(message.getText()).thenReturn("/g");

        // when
        command.onUpdate(update);

        // then
        verify(command).handleCommand(update, 42L, null);
    }

    @Test
    public void shouldPassEmptyArgumentWithoutSpace() throws Exception {

        // given
        when(message.getText()).thenReturn("/g "); //space

        // when
        command.onUpdate(update);

        // then
        verify(command).handleCommand(update, 42L, null);
    }
}