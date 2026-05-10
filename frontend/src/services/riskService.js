import axiosInstance from './axiosInstance';

export const riskService = {
  getRisks: (params) =>
    axiosInstance.get('/risks', { params })
      .then((res) => res.data.data),
  triggerScan: (companyId) =>
    axiosInstance.post('/risks/scan', { companyId })
      .then((res) => res.data.data),
  acknowledge: (id) =>
    axiosInstance.put(`/risks/${id}/acknowledge`)
      .then((res) => res.data.data),
  resolve: (id) =>
    axiosInstance.put(`/risks/${id}/resolve`)
      .then((res) => res.data.data),
  getStats: (companyId) =>
    axiosInstance.get('/risks/stats', { params: { companyId } })
      .then((res) => res.data.data),
};
