import axiosInstance from './axiosInstance';

export const roadmapService = {
  getRoadmap: (userId) =>
    axiosInstance.get('/roadmap', { params: { userId } })
      .then((res) => res.data.data),
  completeStep: (stepId) =>
    axiosInstance.put(`/roadmap/steps/${stepId}/complete`)
      .then((res) => res.data.data),
};
