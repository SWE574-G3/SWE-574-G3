    package com.communitter.api.model;

    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import java.time.LocalDateTime;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    @Table(name="activity_stream")
    public class ActivityStream {

        @Id
        @SequenceGenerator(
                name="activity_stream_sequence",
                sequenceName = "activity_stream_sequence",
                allocationSize = 1
        )
        @GeneratedValue(strategy = GenerationType.SEQUENCE,
                generator = "activity_stream_sequence")
        private Long id;

        @ManyToOne
        @JoinColumn(name = "community_id", nullable = false)
        private Community community;

        @Column(nullable = false)
        private LocalDateTime timestamp;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private ActivityAction action;

        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        private User user;

    //    @ManyToOne
    //    @JoinColumn(name = "comment_id")
    //    private Comment comment;

        @ManyToOne
        @JoinColumn(name = "post_id")
        private Post post;
    }
