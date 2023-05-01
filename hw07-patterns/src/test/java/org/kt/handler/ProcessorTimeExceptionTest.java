package org.kt.handler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kt.model.Message;
import org.kt.processor.homework.ProcessorTime;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class ProcessorTimeExceptionTest {

    @Test
    @DisplayName("Тестируем работу процессор без исключения в нечетную секунду")
    void processorTimeException29s() {
        //given
        var time_29s = LocalDateTime.now().withSecond(29).withNano(0);
        var processorTime = new ProcessorTime(() -> time_29s);

        var message = new Message.Builder(1L).build();

        assertThat(processorTime.process(message)).isEqualTo(message);
    }

    @Test
    @DisplayName("Тестируем исключение в четную секунду")
    void processorTimeException30s() {
        //given
        var time_30s = LocalDateTime.now().withSecond(30).withNano(0);
        var processorTime = new ProcessorTime(() -> time_30s);

        var message = new Message.Builder(1L).build();

        assertThatExceptionOfType(ProcessorTime.TimeException.class).isThrownBy(() -> processorTime.process(message));
    }
}
