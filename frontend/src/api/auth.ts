import request from '@/utils/request'

interface CaptchaVO {
  captchaKey: string
  captchaImage: string
}

interface LoginDTO {
  username: string
  password: string
  captchaCode: string
  captchaKey: string
}

interface LoginVO {
  token: string
  userInfo: UserInfoVO
}

interface UserInfoVO {
  id: number
  username: string
  nickname: string
  avatar: string
  email: string
  phone: string
  status: number
}

export const authApi = {
  getCaptcha: () => request.get<CaptchaVO>('/auth/captcha'),
  login: (data: LoginDTO) => request.post<LoginVO>('/auth/login', data),
  getCurrentUser: () => request.get<UserInfoVO>('/auth/me'),
  logout: () => request.post<void>('/auth/logout'),
}

export type { CaptchaVO, LoginDTO, LoginVO, UserInfoVO }