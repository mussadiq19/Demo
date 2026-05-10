export const queryKeys = {
  dashboard: ['dashboard'],
  risks: {
    list: (params) => ['risks', params],
  },
  skills: {
    gaps: (companyId) => ['skills', 'gaps', companyId],
  },
};
