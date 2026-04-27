import request from '@/utils/request'
import type { UserInfoVO } from './auth'

export const userApi = {
  getUserList: (params: any) => request.get<any>('/user/list', { params }),
  getUserById: (id: number) => request.get<UserInfoVO>(`/user/${id}`),
}