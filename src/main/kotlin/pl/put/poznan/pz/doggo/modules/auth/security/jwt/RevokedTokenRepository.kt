package pl.put.poznan.pz.doggo.modules.auth.security.jwt

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RevokedTokenRepository : JpaRepository<RevokedToken, UUID>