package com.mytest.webapi.model.dto;

import com.mytest.webapi.model.PasswordResetToken;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 11.0.11 (Ubuntu)"
)
@Component
public class PasswordResetTokenMapperImpl implements PasswordResetTokenMapper {

    @Override
    public PasswordResetTokenDto toPasswordTokenDto(PasswordResetToken token) {
        if ( token == null ) {
            return null;
        }

        PasswordResetTokenDto passwordResetTokenDto = new PasswordResetTokenDto();

        passwordResetTokenDto.setId( token.getId() );
        passwordResetTokenDto.setToken( token.getToken() );
        passwordResetTokenDto.setUser( token.getUser() );
        passwordResetTokenDto.setExpiryDates( token.getExpiryDates() );

        return passwordResetTokenDto;
    }

    @Override
    public PasswordResetToken toPasswordToken(PasswordResetTokenDto token) {
        if ( token == null ) {
            return null;
        }

        PasswordResetToken passwordResetToken = new PasswordResetToken();

        passwordResetToken.setId( token.getId() );
        passwordResetToken.setToken( token.getToken() );
        passwordResetToken.setUser( token.getUser() );
        passwordResetToken.setExpiryDates( token.getExpiryDates() );

        return passwordResetToken;
    }
}
