package pl.put.poznan.pz.doggo.modules.auth.security.authentication

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import pl.put.poznan.pz.doggo.infrastructure.exceptions.TokenNotFoundException
import pl.put.poznan.pz.doggo.infrastructure.exceptions.UserAlreadyExistsException
import pl.put.poznan.pz.doggo.infrastructure.jwt.JwtUtils
import pl.put.poznan.pz.doggo.modules.auth.dto.requests.LoginRequestDTO
import pl.put.poznan.pz.doggo.modules.auth.dto.requests.SignUpRequestDTO
import pl.put.poznan.pz.doggo.modules.auth.dto.responses.JwtTokenInfoDTO
import pl.put.poznan.pz.doggo.modules.auth.dto.user.UserInfoDTO
import pl.put.poznan.pz.doggo.modules.auth.security.jwt.JwtBlacklistService
import pl.put.poznan.pz.doggo.modules.auth.security.userdetails.CustomUserDetails
import pl.put.poznan.pz.doggo.modules.auth.user.UserEntity
import pl.put.poznan.pz.doggo.modules.auth.user.UserEntityRepository

@Service
class AuthenticationService(
        private val jwtBlacklistService: JwtBlacklistService,
        private val authenticationManager: AuthenticationManager,
        private val passwordEncoder: PasswordEncoder,
        private val userEntityRepository: UserEntityRepository,
        private val jwtUtils: JwtUtils
) {

    fun authenticateUser(loginRequest: LoginRequestDTO): JwtTokenInfoDTO {
        val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(loginRequest.email, loginRequest.password)
        )
        SecurityContextHolder.getContext().authentication = authentication
        val jwt = jwtUtils.generateToken(authentication)

        return JwtTokenInfoDTO(token = jwt)
    }

    fun registerUser(signUpRequestDTO: SignUpRequestDTO): UserInfoDTO {
        checkIfUserAlreadyExists(signUpRequestDTO.email)
        val user = UserEntity(
                email = signUpRequestDTO.email,
                password = passwordEncoder.encode(signUpRequestDTO.password),
                roles = signUpRequestDTO.getEnumRoles()
        )
        return UserInfoDTO(userEntityRepository.save(user))
    }

    fun signOutUser(authHeader: String): String {
        val token = jwtUtils.parseJwt(authHeader) ?: throw TokenNotFoundException()
        jwtBlacklistService.revokeToken(token)
        return "Successfully logged out."
    }

    private fun checkIfUserAlreadyExists(email: String) {
        if (userEntityRepository.existsByEmail(email))
            throw UserAlreadyExistsException(email)
    }
}