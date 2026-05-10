import axiosInstance from '../lib/axios';

export const notificationService = {
  getAll: () => axiosInstance.get('/notifications').then((r) => r.data.data),
  markRead: (id) => axiosInstance.put(`/notifications/${id}/read`).then((r) => r.data.data),
};
