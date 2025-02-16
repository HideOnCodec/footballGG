package com.footballgg.server.repository.file;

import com.footballgg.server.domain.file.FileMapping;
import com.footballgg.server.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FileMappingRepository extends JpaRepository<FileMapping,Long> {
    Optional<FileMapping> findByFileUrl(String fileUrl);
    List<FileMapping> findAllByPost(Post post);
    @Query("SELECT fm.fileUrl FROM FileMapping fm WHERE fm.post IS NULL AND fm.createDate <= :date")
    List<String> findAllByPostAndCreateDateBefore(LocalDateTime date);
    void deleteAllByFileUrlIn(List<String> fileUrls);
}
