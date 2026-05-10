import axiosInstance from './axiosInstance';

export const roadmapService = {
  getRoadmap: (userId) =>
    axiosInstance.get(`/roadmap/${userId}`)
      .then((res) => res.data.data),
  completeStep: (stepId) =>
    axiosInstance.put(`/roadmap/steps/${stepId}/complete`)
      .then((res) => res.data.data),
  regenerate: (userId) =>
    axiosInstance.post('/roadmap/regenerate', { userId })
      .then((res) => res.data.data),
};
