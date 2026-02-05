import { StyledSignUpContainer } from '@/styles/signUpStyle';
import { useState } from 'react';
import RegistrationLayout from '../registrationLayout/RegistrationLayout';
import SignUpForm from './SignUpForm';
import SignUpSuccess from './SignUpSuccess';

const SignUp = () => {
  const [signUpSuccess, setSignUpSuccess] = useState(false);
  return (
    <RegistrationLayout>
      {signUpSuccess ? (
        <StyledSignUpContainer>
          <SignUpSuccess />
        </StyledSignUpContainer>
      ) : (
        <StyledSignUpContainer sx={{ width: { md: 'auto', xs: '100%' } }}>
          <SignUpForm setSignUpSuccess={setSignUpSuccess} />
        </StyledSignUpContainer>
      )}
    </RegistrationLayout>
  );
};

export default SignUp;
