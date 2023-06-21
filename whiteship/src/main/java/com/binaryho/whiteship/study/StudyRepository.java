package com.binaryho.whiteship.study;

import com.binaryho.whiteship.domain.Study;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRepository extends JpaRepository<Study, Long> {

}
