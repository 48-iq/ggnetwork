package ru.ivanov.ggnetwork.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.ivanov.ggnetwork.dto.group.GroupCreateDto;
import ru.ivanov.ggnetwork.repositories.GroupRepository;

import java.util.regex.Pattern;

@Component
public class GroupCreateDtoValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return GroupCreateDto.class.equals(clazz);
    }

    @Autowired
    private GroupRepository groupRepository;

    @Override
    public void validate(Object target, Errors errors) {
        var groupCreateDto = (GroupCreateDto) target;
        if (groupCreateDto == null) {
            errors.reject("groupCreateDto","group create dto is null");
            return;
        }
        if (groupCreateDto.getIcon() != null) {
            if (groupCreateDto.getIcon().getSize() > 1024 * 1024 * 100)
                errors.reject("icon", "icon size > 100MB");
        }
        System.out.println(groupCreateDto.getTitle());
        System.out.println(groupCreateDto);
        if (groupCreateDto.getTitle() == null)
            errors.reject("title", "title is null");
        else {
            var pattern = Pattern.compile("^.{0,64}$");
            var matcher = pattern.matcher(groupCreateDto.getTitle());
            if (!matcher.matches())
                errors.reject("title", "title is invalid");
            else if (groupRepository.existsByTitle(groupCreateDto.getTitle()))
                errors.reject("title", "title already exists");
        }
        if (groupCreateDto.getDescription() != null) {
            var pattern = Pattern.compile("^.{0,255}$");
            var matcher = pattern.matcher(groupCreateDto.getDescription());
            if (!matcher.matches())
                errors.reject("description", "description is invalid");
        }
    }
}
