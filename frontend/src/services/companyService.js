import axiosInstance from './axiosInstance';

export const companyService = {
  getDashboard: (companyId) =>
    axiosInstance.get(`/companies/${companyId}/dashboard`)
      .then((res) => res.data.data),
  getCompany: (companyId) =>
    axiosInstance.get(`/companies/${companyId}`)
      .then((res) => res.data.data),
};
