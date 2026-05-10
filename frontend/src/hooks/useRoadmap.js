import { useQuery } from '@tanstack/react-query';
import axiosInstance from '../lib/axios';
import { useAuth } from './useAuth';

export function useRoadmap() {
  const { user } = useAuth();
  return useQuery({
    queryKey: ['roadmap', user?.id],
    queryFn: () => axiosInstance
      .get(`/roadmap/${user.id}`)
      .then(r => r.data.data),
    enabled: !!user?.id,
  });
}
