import CustomButton from '@/components/common/Button/CustomButton';
import Input from '@/components/common/Input/Input';
import TextLg from '@/components/common/Text/TextLg';
import TextMd from '@/components/common/Text/TextMd';
import TextXs from '@/components/common/Text/TextXs';
import { Check, CrossEye, Eye, UnCheck } from '@/constants/images.routes';
import { COMMON_MESSAGES } from '@/constants/locales';
import { FORGET_PASSWORD, SIGN_UP } from '@/constants/routes/pages.routes';
import {
  StyledBottomLink,
  StyledFormControlBox,
  StyledInputBox,
  StyledLoginContainer,
  StyledLoginSubContainer,
} from '@/styles/loginStyle';
import { handleTogglePassword } from '@/utils/helperFunctions';
import { Checkbox, FormControlLabel, Stack } from '@mui/material';
import Image from 'next/image';
import Link from 'next/link';
import { useState } from 'react';
import RegistrationLayout from '../registrationLayout/RegistrationLayout';
import axios from 'axios';
import { toast } from 'react-toastify';
import { useRouter } from 'next/router';

const Login = () => {
  const router = useRouter();
  const [showPassword, setShowPassword] = useState(true);
  const [checkbox, setCheckbox] = useState(false);
  const [loading, setLoading] = useState(false);

  const [formData, setFormData] = useState({
    email: '',
    password: '',
  });

  const handleInputChange = (field: string) => (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [field]: e.target.value });
  };

  const handleCheckboxChange = () => {
    setCheckbox(!checkbox);
  };

  const handleSubmit = async () => {
    if (!formData.email || !formData.password) {
      toast.error('Please enter email and password');
      return;
    }

    setLoading(true);
    try {
      const apiUrl = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:3002';
      // The backend login endpoint expects LoginDto
      await axios.post(`${apiUrl}/v1/api/auth/login`, {
        email: formData.email,
        password: formData.password,
        auth_id: formData.email, // using email as auth_id for simplicity
        type: 'email',
      });

      toast.success('Login successful!');
      router.push('/');
    } catch (error: any) {
      console.error('Login error:', error);
      toast.error(error.response?.data?.message || 'Failed to log in');
    } finally {
      setLoading(false);
    }
  };

  return (
    <RegistrationLayout>
      <StyledLoginContainer>
        <StyledLoginSubContainer>
          <TextLg
            sx={{
              fontWeight: '400',
              lineHeight: '133.4%',
              color: 'var(--text-black)',
              textAlign: { md: 'left', xs: 'center' },
            }}
            text="Log In"
          />

          <StyledInputBox>
            <Input label={COMMON_MESSAGES.EMAIL_ADDRESS} type="email" value={formData.email} onChange={handleInputChange('email')} />
            <Input
              endAdornment={
                <Stack
                  onClick={() => {
                    handleTogglePassword(setShowPassword, showPassword);
                  }}
                  sx={{
                    cursor: 'pointer',
                  }}
                >
                  {showPassword ? (
                    <Image src={CrossEye} width={20} height={20} alt="pic" />
                  ) : (
                    <Image src={Eye} width={20} height={20} alt="pic" />
                  )}
                </Stack>
              }
              label={COMMON_MESSAGES.PASSWORD}
              type={showPassword ? 'password' : 'text'}
              value={formData.password}
              onChange={handleInputChange('password')}
            />
          </StyledInputBox>

          <StyledFormControlBox>
            <FormControlLabel
              control={
                <Checkbox
                  checked={checkbox}
                  onChange={handleCheckboxChange}
                  icon={<Image priority src={UnCheck} alt={'icon'} width={24} height={24} />}
                  checkedIcon={<Image priority src={Check} alt={'icon'} width={24} height={24} />}
                  sx={{
                    padding: '0rem',
                  }}
                />
              }
              label={
                <TextMd
                  text={COMMON_MESSAGES.REMEMBER_ME}
                  sx={{
                    fontSize: '1rem',
                    fontWeight: '400',
                    lineHeight: '150%',
                    letterSpacing: '0.00938rem',
                    color: 'var(--text-black)',
                    marginLeft: '0.63rem',
                  }}
                />
              }
            />
          </StyledFormControlBox>

          <CustomButton
            onClick={handleSubmit}
            disabled={loading}
            text={loading ? 'Checking...' : COMMON_MESSAGES.SIGN_IN}
            sx={{
              marginTop: '2.06rem',
              backgroundColor: 'var(--sky-blue)',
              borderRadius: '0.25rem',
              fontSize: '0.9375rem',
            }}
          />
          <StyledBottomLink>
            <Link href={SIGN_UP}>
              <TextXs
                text="Create account"
                sx={{
                  fontSize: '0.875rem',
                  fontWeight: '400',
                  lineHeight: '143%',
                  letterSpacing: '0.01063rem',
                  color: 'var(--skyblue-100)',
                }}
              />
            </Link>

            <Link href={FORGET_PASSWORD}>
              <TextXs
                sx={{
                  fontSize: '0.875rem',
                  fontWeight: '400',
                  lineHeight: '143%',
                  letterSpacing: '0.01063rem',
                  color: 'var(--skyblue-100)',
                }}
                text="Forgot password?"
              />
            </Link>
          </StyledBottomLink>
        </StyledLoginSubContainer>
      </StyledLoginContainer>
    </RegistrationLayout>
  );
};

export default Login;
