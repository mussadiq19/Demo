import axiosInstance from './axiosInstance';

export const companyService = {
  getDashboard: (companyId) =>
    axiosInstance.get(`/companies/${companyId}/dashboard`)
      .then((res) => res.data.data),
  getCompany: (companyId) =>
    axiosInstance.get(`/companies/${companyId}`)
      .then((res) => res.data.data),
  updateCompany: (companyId, payload) =>
    axiosInstance.put(`/companies/${companyId}`, payload)
      .then((res) => res.data.data),
  getAiReadiness: (companyId) =>
    axiosInstance.get(`/companies/${companyId}/ai-readiness`)
      .then((res) => res.data.data),
};
