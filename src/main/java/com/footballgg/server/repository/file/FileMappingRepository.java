package com.footballgg.server.repository.file;

import com.footballgg.server.domain.file.FileMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileMappingRepository extends JpaRepository<FileMapping,Long> {
    Optional<FileMapping> findByFileUrl(String fileUrl);
}
