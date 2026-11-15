package com.github.aabushmeleva.blog.service;

import com.github.aabushmeleva.blog.service.event.PostDeletedEvent;
import com.github.aabushmeleva.blog.storage.ImageStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PostImageCleanupListener {

    private final ImageStorage imageStorage;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPostDeleted(PostDeletedEvent event) {
        if (event.imageName() != null) {
            imageStorage.delete(event.imageName());
        }
    }
}
