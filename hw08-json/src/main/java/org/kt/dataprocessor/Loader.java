package org.kt.dataprocessor;

import org.kt.model.Measurement;

import java.util.List;

public interface Loader {

    List<Measurement> load();
}
