package me.jincrates.pf.assignment.application.repository;

import java.util.List;
import me.jincrates.pf.assignment.domain.model.Sample;

public interface NativeJpaRepository {

    List<Sample> saveAll(List<Sample> samples);
}
