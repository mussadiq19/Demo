import axiosInstance from './axiosInstance';

export const skillsService = {
  getGaps: (companyId) =>
    axiosInstance.get('/skills/gaps', { params: { companyId } })
      .then((res) => res.data.data),
  upload: (file) => {
    const formData = new FormData();
    formData.append('file', file);
    return axiosInstance.post('/skills/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    }).then((res) => res.data.data);
  },
};
