package sparta.task.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sparta.task.domain.model.UploadFile;
import sparta.task.domain.repository.UploadFileRepository;
import sparta.task.infrastructure.exception.HttpStatusException;
import sparta.task.infrastructure.exception.constants.ErrorCode;
import sparta.task.infrastructure.repository.jpa.UploadFileJpaRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UploadFileRepositoryImpl implements UploadFileRepository {
    private final UploadFileJpaRepository uploadFileJpaRepository;

    @Override
    public Optional<UploadFile> findByFilename(String filename) {
        return uploadFileJpaRepository.findByFilename(filename);
    }

    @Override
    public UploadFile getByFilename(String filename) {
        return uploadFileJpaRepository.findByFilename(filename)
                .orElseThrow(() -> new HttpStatusException(ErrorCode.NOT_FOUND));
    }

    @Override
    public Optional<UploadFile> findById(Long id) {
        return uploadFileJpaRepository.findById(id);
    }

    @Override
    public UploadFile getById(Long id) {
        return uploadFileJpaRepository.findById(id)
                .orElseThrow(() -> new HttpStatusException(ErrorCode.NOT_FOUND));
    }

    @Override
    public UploadFile save(UploadFile uploadFile) {
        return uploadFileJpaRepository.save(uploadFile);
    }

    @Override
    public void deleteById(Long id) {
        uploadFileJpaRepository.deleteById(id);

    }
}
