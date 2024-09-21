package ru.ivanov.ggnetwork.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.ivanov.ggnetwork.dto.group.GroupCreateDto;
import ru.ivanov.ggnetwork.dto.group.GroupUpdateDto;
import ru.ivanov.ggnetwork.repositories.GroupRepository;

import java.util.regex.Pattern;

@Component
public class GroupUpdateDtoValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return GroupUpdateDto.class.equals(clazz);
    }

    @Autowired
    private GroupRepository groupRepository;

    @Override
    public void validate(Object target, Errors errors) {
        var groupUpdateDto = (GroupUpdateDto) target;
        if (groupUpdateDto == null) {
            errors.reject("group update dto is null");
            return;
        }
        if (groupUpdateDto.getIcon() != null) {
            if (groupUpdateDto.getIcon().getSize() > 1024 * 1024 * 100)
                errors.reject("icon", "icon size > 100MB");
        }
        if (groupUpdateDto.getTitle() == null)
            errors.reject("title", "title is null");
        else {
            var pattern = Pattern.compile("^.{0,64}$");
            var matcher = pattern.matcher(groupUpdateDto.getTitle());
            if (!matcher.matches())
                errors.reject("title", "title is invalid");
            else if (groupRepository.existsByTitle(groupUpdateDto.getTitle()))
                errors.reject("title", "title already exists");
        }
        if (groupUpdateDto.getDescription() != null) {
            var pattern = Pattern.compile("^.{0,255}$");
            var matcher = pattern.matcher(groupUpdateDto.getDescription());
            if (!matcher.matches())
                errors.reject("description", "description is invalid");
        }
    }
}
