package com.github.throyer.common.springboot.controllers.app;

import com.github.throyer.common.springboot.domain.toast.Type;
import com.github.throyer.common.springboot.domain.user.service.FindUserService;
import com.github.throyer.common.springboot.domain.user.service.RemoveUserService;
import com.github.throyer.common.springboot.domain.toast.Toasts;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@PreAuthorize("hasAnyAuthority('ADM')")
@RequestMapping("/app/users")
public class UserController {
    
    @Autowired
    private FindUserService findService;
    
    @Autowired
    private RemoveUserService removeService;
    
    @GetMapping
    public String index(
        Model model,
        Optional<Integer> page,
        Optional<Integer> size
    ) {
        
        var result = findService.findAll(page, size);
        
        model.addAttribute("page", result);
        
        return "app/users/index";
    }
    
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        removeService.remove(id);
        
        Toasts.add(redirect, "Usuário deletado com sucesso.", Type.SUCCESS);
        
        return "redirect:/app/users";
    }
}
