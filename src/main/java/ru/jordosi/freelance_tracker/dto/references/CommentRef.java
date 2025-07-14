package ru.jordosi.freelance_tracker.dto.references;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.jordosi.freelance_tracker.model.Comment;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRef {
    private Long id;
    private String text;

    public CommentRef(Comment comment) {
        this.id = comment.getId();
        this.text = comment.getText();
    }
}
