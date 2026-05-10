import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { notificationService } from '../services/notificationService';
import { useAuth } from './useAuth';

export function useNotifications() {
  const { user } = useAuth();
  const queryClient = useQueryClient();
  const { data: notifications = [], isLoading } = useQuery({
    queryKey: ['notifications', user?.id],
    queryFn: notificationService.getAll,
    enabled: !!user?.id,
    refetchInterval: 15000,
  });
  const unreadCount = notifications.filter((n) => !n.read).length;
  const { mutate: markRead } = useMutation({
    mutationFn: notificationService.markRead,
    onSuccess: () => queryClient.invalidateQueries(['notifications']),
  });
  return { notifications, unreadCount, markRead, isLoading };
}
