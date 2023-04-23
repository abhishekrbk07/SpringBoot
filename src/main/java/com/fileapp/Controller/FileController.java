package com.fileapp.Controller;

import com.fileapp.model.AuthRequest;
import com.fileapp.service.DimensionServiceImp;
import com.fileapp.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController

public class FileController {

    @Autowired
    private DimensionServiceImp dimensionServiceImp;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/posts")
    String getPost(){
        return "posts";
    }

    @GetMapping("/dimensions/{id}")
    public ResponseEntity<?>getDimensions(@PathVariable("id") Integer dimensionId){
      List<String> list = dimensionServiceImp.generate(dimensionId);
       return new ResponseEntity<>(list , HttpStatus.OK);
    }

    @Value("${resource.file.location}")
    private String filePath;
    @GetMapping("/download/{id}")
    public ResponseEntity<?>downloadDimensions(@PathVariable("id") Integer dimensionId){
        try {
                    if (Files.exists(Path.of(filePath))) {
                        FileSystemResource resource = new FileSystemResource(filePath);

                    // Return a ResponseEntity containing the Resource as the response body
                    return ResponseEntity.ok()
                            .contentType(MediaType.parseMediaType("text/csv"))
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "dimension"  + "\"")
                            .body(resource);

                } else {
                    return dimensionServiceImp.download(dimensionId);
                }
            } catch (Exception exception) {
                // Handle any exceptions that may occur
                exception.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        @PostMapping("/authentication")
        public String authenticateAndGenerateToken(@RequestBody AuthRequest authRequest) {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
            if (authentication.isAuthenticated()) {
                return jwtService.generateToken(authRequest.getUserName());
            } else {
                throw new UsernameNotFoundException("invalid user request !");
            }
        }
    }


