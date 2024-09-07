package ru.ivanov.ggnetwork.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "personal_chats",
    uniqueConstraints = @UniqueConstraint(columnNames = {"first_user_id", "second_user_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalChat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "first_user_id", referencedColumnName = "id", nullable = false)
    private User firstUser;

    @ManyToOne
    @JoinColumn(name = "second_user_id", referencedColumnName = "id", nullable = false)
    private User secondUser;
}
