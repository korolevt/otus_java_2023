package org.kt.handler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kt.model.Message;
import org.kt.processor.homework.ProcessorTime;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ProcessorTimeExceptionTest {

    @Test
    @DisplayName("Тестируем исключение в четную секунду")
    void processorTimeException() {
        //given
        var time_30s = LocalDateTime.now().withSecond(30).withNano(0);
        var processorTime = new ProcessorTime(() -> time_30s);

        var message = new Message.Builder(1L).build();
        assertThatExceptionOfType(ProcessorTime.TimeException.class).isThrownBy(() -> processorTime.process(message));
    }
}
