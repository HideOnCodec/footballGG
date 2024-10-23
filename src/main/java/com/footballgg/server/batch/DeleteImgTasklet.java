package com.footballgg.server.batch;

import com.footballgg.server.domain.file.FileMapping;
import com.footballgg.server.repository.file.FileMappingRepository;
import com.footballgg.server.service.file.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeleteImgTasklet implements Tasklet {
    private final FileService fileService;
    private final FileMappingRepository fileMappingRepository;
    /**
     * 게시글과 매핑되지 않은 이미지 파일 삭제 처리
     * 생성된지 이틀이 지난 경우 삭제
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("start deleting images : {}", LocalDateTime.now());
        List<String> deleteList = fileMappingRepository.findAllByPostAndCreateDateBefore(LocalDateTime.now().minusDays(2));
        fileService.deleteMultiFile(deleteList);
        return RepeatStatus.FINISHED;
    }
}
