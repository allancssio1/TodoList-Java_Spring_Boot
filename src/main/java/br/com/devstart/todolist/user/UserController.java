package br.com.devstart.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired // spring vai gerenciar o ciclo de vida do repositório e seus médotos.
  private IUserRepository userRepository;

  @PostMapping("/")
  public ResponseEntity create (@RequestBody UserModel userModel ) {
    var user = this.userRepository.findByUsername(userModel.getUsername());

    if(user != null) {
      System.out.println("usuário já existe.");

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe");
    }

    var passwordHach = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());

    userModel.setPassword(passwordHach);

    var userCreated = this.userRepository.save(userModel);

    return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);

  }
}
